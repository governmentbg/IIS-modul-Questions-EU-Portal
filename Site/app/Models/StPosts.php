<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $StP_id
 * @property DateTime   $StP_date
 * @property string     $StP_title
 * @property string     $StP_body
 * @property int        $StP_uid
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class StPosts extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'St_Posts';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'StP_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'StP_id', 'StS_id' => 'int', 'StP_date', 'StP_title', 'StP_body', 'StP_uid', 'created_at', 'updated_at', 'deleted_at'
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
        'StP_id' => 'int', 'StS_id' => 'int', 'StP_date' => 'datetime', 'StP_title' => 'string', 'StP_body' => 'string', 'StP_uid' => 'int', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
    ];

    /**
     * The attributes that should be mutated to dates.
     *
     * @var array
     */
    protected $dates = [
        'StP_date', 'created_at', 'updated_at', 'deleted_at'
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
            $article->created_at = now();
            $article->updated_at = now();
        });

        static::saving(function ($article) {
            // $article->X_User_id = auth()->user()->id;
            $article->updated_at = now();
        });
    }

    // Scopes...

    // Functions ...

    // Relations ...
    public function eq_section()
    {
        return $this->belongsTo(StSection::class, 'StS_id');
    }

    public function eq_files()
    {
        return $this->hasMany(StFiles::class, 'StP_id');
    }
}
