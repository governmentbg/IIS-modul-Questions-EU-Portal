<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

/**
 * @property int        $ID
 * @property int        $NOMER
 * @property Date       $DAT_BULETIN
 * @property string     $DOP_INFO
 * @property string     $FILE
 * @property string     $ZAGLAVIE
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class EUROBULETIN extends Model
{
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'EURO_BULETIN';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'ID';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'ID', 'NOMER', 'DAT_BULETIN', 'DOP_INFO', 'FILE', 'ZAGLAVIE', 'created_at', 'updated_at', 'deleted_at'
    ];

    /**
     * The attributes excluded from the model's JSON form.
     *
     * @var array
     */
    protected $hidden = [];

    /**
     * The attributes that should be casted to native types.
     *
     * @var array
     */
    protected $casts = [
        'ID' => 'int', 'NOMER' => 'int', 'DAT_BULETIN' => 'date', 'DOP_INFO' => 'string', 'FILE' => 'string', 'ZAGLAVIE' => 'string', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
    ];

    /**
     * The attributes that should be mutated to dates.
     *
     * @var array
     */
    protected $dates = [
        'DAT_BULETIN', 'created_at', 'updated_at', 'deleted_at'
    ];

    /**
     * Indicates if the model should be timestamped.
     *
     * @var boolean
     */
    public $timestamps = false;


    public static function boot()
    {
        parent::boot();

        static::creating(function ($article) {
            $article->created_at = now();
            $article->updated_at = now();
        });

        static::saving(function ($article) {
            $article->updated_at = now();
        });
    }

    // Scopes...

    // Functions ...

    // Relations ...
}
