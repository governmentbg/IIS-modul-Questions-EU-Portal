<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $Vid
 * @property Date       $Vidate
 * @property int        $Vicount
 * @property int        $A_ns_C_id
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class VideoArchive extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'Video_Archive';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'Vid';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'Vid', 'Vidate', 'Vicount', 'A_ns_C_id', 'created_at', 'updated_at', 'deleted_at'
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
        'Vid' => 'int', 'Vidate' => 'date', 'Vicount' => 'int', 'A_ns_C_id' => 'int', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
    ];

    /**
     * The attributes that should be mutated to dates.
     *
     * @var array
     */
    protected $dates = [
        'Vidate', 'created_at', 'updated_at', 'deleted_at'
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
            // $article->X_User_id = auth()->user()->id;
            $article->A_ns_C_id = currentNSCID();
            $article->created_at = now();
            $article->updated_at = now();
        });

        static::saving(function ($article) {
            // $article->X_User_id = auth()->user()->id;
            $article->A_ns_C_id = currentNSCID();
            $article->updated_at = now();
        });
    }

    // Scopes...

    // Functions ...

    // Relations ...
}
