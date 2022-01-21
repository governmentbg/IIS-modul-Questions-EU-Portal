<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $D_Pt_id
 * @property string     $D_Pt_author
 * @property string     $D_Pt_email
 * @property string     $D_Pt_topic
 * @property Date       $D_Pt_date
 * @property string     $D_Pt_body
 * @property string     $D_Pt_file
 * @property string     $D_Pt_key
 * @property int        $D_Pt_confirm
 * @property int        $C_St_id
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class DPetition extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'D_Petition';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'D_Pt_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'D_Pt_id', 'D_Pt_author', 'D_Pt_email', 'D_Pt_topic', 'D_Pt_date', 'D_Pt_body', 'D_Pt_file', 'D_Pt_key', 'D_Pt_confirm', 'C_St_id', 'created_at', 'updated_at', 'deleted_at'
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
        'D_Pt_id' => 'int', 'D_Pt_author' => 'string', 'D_Pt_email' => 'string', 'D_Pt_topic' => 'string', 'D_Pt_date' => 'date', 'D_Pt_body' => 'string', 'D_Pt_file' => 'string', 'D_Pt_key' => 'string', 'D_Pt_confirm' => 'timestamp', 'C_St_id' => 'int', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
    ];

    /**
     * The attributes that should be mutated to dates.
     *
     * @var array
     */
    protected $dates = [
        'D_Pt_date', 'D_Pt_confirm', 'created_at', 'updated_at', 'deleted_at'
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
